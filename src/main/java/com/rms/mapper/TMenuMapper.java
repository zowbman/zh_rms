package com.rms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import tk.mybatis.mapper.common.Mapper;

import com.rms.model.po.TMenu;
import com.rms.model.po.TMenuCustom;
import com.rms.model.po.TPrivilege;

/**
 * 
 * Title:TMenuMapper
 * Description:菜单mapper接口
 * @author    zwb
 * @date      2016年9月29日 下午12:01:12
 *
 */
public interface TMenuMapper extends Mapper<TMenu>  {
	
	/**
	 * 查询顶级从菜单（关联查询从菜单的子菜单和对应权限）
	 * @return
	 */
	@Select("SELECT * FROM t_menu WHERE status = 0 AND menuType = 1 AND parentId is Null")
	@Results({
		@Result(property = "slaveChildrenMenus", 
				column = "id",
				many = @Many(select = "findTopSlaveMenusByParentId")),
		@Result(property = "privilege",
				column = "id",
				one =@One(select ="findPrivilegeByMenuId"))
	})
	public List<TMenuCustom> findTopSlaveMenusAndPrivilege();
	
	
	/**
	 * 根据父菜单id查询菜单
	 * @param parentId 父菜单id
	 * @return List<TMenuCustom>
	 */
	@Select("SELECT * FROM t_menu WHERE status = 0 AND parentId = #{parentId}")
	@Results({
		@Result(property = "slaveChildrenMenus",
				column = "id",
				many = @Many(select = "findTopSlaveMenusByParentId"))
	})
	public List<TMenuCustom> findTopSlaveMenusByParentId(Integer parentId);
	
	/**
	 * 根据菜单id查询权限
	 * @param menuId 菜单id
	 * @return TPrivilege
	 */
	@Select("SELECT * FROM t_privilege WHERE menuId = #{menuId}")
	public TPrivilege findPrivilegeByMenuId(Integer menuId);
}